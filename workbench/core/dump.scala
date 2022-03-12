package njumips

import chisel3._
import chisel3.util._
import njumips.consts._
import njumips.configs._
import njumips.utils._
import njumips.core._

import scala.reflect._
import scala.reflect.runtime.{universe => ru}
import scala.reflect.macros.blackbox._

object printv {
  val dftFmtMap = Map[String,String](
    "func" -> "%d", "et" -> "%d", "code" -> "%d",
    "instr" -> "%x", "rf_dirtys" -> "%b",
    "fu_type" -> "%d", "fu_op" -> "%d",
    "bp_readys" -> "%b", "cpr_index" -> "%x",
    "cpr_entry_lo0" -> "%x", "cpr_entry_lo1" -> "%x",
    "cpr_context" -> "%x", "cpr_pagemask" -> "%x",
    "cpr_wired" -> "%x", "cpr_base" -> "%x",
    "cpr_badvaddr" -> "%x", "cpr_count" -> "%x",
    "cpr_entry_hi" -> "%x", "cpr_compare" -> "%x",
    "cpr_status" -> "%x", "cpr_cause" -> "%x",
    "cpr_epc" -> "%x", "cpr_prid" -> "%x",
    "cpr_ebase" -> "%x", "cpr_config" -> "%x",
    "cpr_config1" -> "%x"
    )

  def getTypeFromString(name: String): ru.Type = {
    val c = Class.forName(name)
    val mirror = ru.runtimeMirror(c.getClassLoader)
    val sym = mirror.staticClass(name)
    sym.selfType
  }

  private def traverse(obj: Any, tp: ru.Type, fmtmap:Map[String,String]=dftFmtMap): (String, Seq[Bits]) = {
    if (tp =:= ru.typeOf[UInt] || tp =:= ru.typeOf[SInt]
      || tp =:= ru.typeOf[Bool]) {
      return ("%x", Seq[Bits](obj.asInstanceOf[Bits]))
    }

    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    val objMirror = mirror.reflect(obj)
    var members = tp.members.filter(m => m.isPublic && m.isMethod && m.asMethod.returnType <:< ru.typeOf[Data])
      .filter(m => !m.isConstructor && m.isMethod && m.info.paramLists.isEmpty && !m.info.takesTypeArgs)
      .filter(m => !(m.asMethod.returnType =:= tp) && m.name.toString != "cloneType" && m.name.toString != "io" && m.name.toString.exists(_.isLower))
      .toList.sortWith(_.name.toString < _.name.toString)
    var fmtString = ""
    var fmtBits = Seq[Bits]()
    var isBits = false
    if (tp <:< ru.typeOf[ValidIO[_]]) {
      val valid = objMirror.reflectMethod(members.filter(
        _.name.toString == "valid").head.asMethod)()
      fmtString = fmtString+"[%b]"
      fmtBits=fmtBits++Seq[Bits](valid.asInstanceOf[Bits])
      members = members.filter(_.name.toString == "bits")
      isBits = true
    } else if (tp <:< ru.typeOf[DecoupledIO[_]]) {
      val valid = objMirror.reflectMethod(members.filter(
        _.name.toString == "valid").head.asMethod)()
      val ready = objMirror.reflectMethod(members.filter(
        _.name.toString == "ready").head.asMethod)()
      fmtString = fmtString+"[%b,%b]"
      fmtBits=fmtBits++Seq[Bits](valid.asInstanceOf[Bits], ready.asInstanceOf[Bits])
      members = members.filter(_.name.toString == "bits")
      isBits = true
    }
    members.foreach { m => {
        val value = objMirror.reflectMethod(m.asMethod)()
        val info = if(isBits)
          getTypeFromString(value.getClass.getName)
          else m.asMethod.returnType
        val name = if(isBits) "" else m.name.toString
        if (fmtmap.contains(name)) {
          fmtString = fmtString+name+"="+
            fmtmap.getOrElse(name, "%x") + " "
          fmtBits = fmtBits++Seq[Bits](value.asInstanceOf[Data].asUInt)
        } else if ((info <:< ru.typeOf[ValidIO[_]]) ||
          (info <:< ru.typeOf[DecoupledIO[_]])) {
          val ret = traverse(value, info, fmtmap)
          fmtString = fmtString+name+ret._1.trim+" "
          fmtBits = fmtBits++ret._2
        } else if ((info <:< ru.typeOf[Bundle])) {
          val ret = traverse(value, info, fmtmap)
          fmtString = fmtString+name+"={"+
            ret._1.trim+"} "
          fmtBits = fmtBits++ret._2
        } else if (info <:< ru.typeOf[Bits]) {
          fmtString = fmtString+name+"=%x "
          fmtBits = fmtBits++Seq[Bits](value.asInstanceOf[Bits])
        }
      }
    }
    (fmtString.trim, fmtBits)
  }

  def traverseMemberValues[T](topLevelObj: T, fmtmap:Map[String,String]=dftFmtMap)(implicit c: ru.TypeTag[T]) = {
    val ret = traverse(topLevelObj, c.tpe, fmtmap)
    ret
  }

  def apply[T:ru.TypeTag](sig:T, module:String, fmtmap:Map[String,String]=dftFmtMap) = {
    val ret = traverseMemberValues(sig, fmtmap)
    val bits = Seq[Bits](GTimer())++ret._2
    printf("%d: "+module+": "+ret._1+"\n", bits:_*)
  }

  def memdump[T<:Data:ru.TypeTag](mem:Mem[T], msg:String, fmtmap:Map[String,String]=dftFmtMap) = {
    var fmts = ""
    var bits = Seq[Bits](GTimer())
    for (i <- 0 until mem.length.toInt) {
      val ret = traverseMemberValues(mem(i), fmtmap)
      fmts=fmts+ret._1 + " "
      bits=bits++ret._2
    }
    printf("%d: "+msg+": "+fmts.trim+"\n", bits:_*)
  }

  def apply(module:String, sigs:Array[(String,Data)]) {
    var fmts = ""
    var bits = Seq[Bits](GTimer())
    for ((name, sig) <- sigs) {
      val ret = traverse(sig, getTypeFromString(sig.getClass.getName))
      fmts=fmts+name+"={"+ret._1.trim+"} "
      bits=bits++ret._2
    }
    printf("%d: "+module+": "+fmts.trim+"\n", bits:_*)
  }
}

