package njumips





class verilator_top extends Module {
  val io = IO(new Bundle {
    val commit = new CommitIO   // defined in interfaces.scala
    val can_log_now = Input(Bool())
  })

  // TODO
}



object TopMain extends App {
  override def main(args:Array[String]):Unit = {
    val top = args(0)
    val chiselArgs = args.slice(1, args.length)
    chisel3.Driver.execute(chiselArgs, () => {
      val clazz = Class.forName("njumips.core."+top)
      val constructor = clazz.getConstructor()
      constructor.newInstance().asInstanceOf[Module]
    })
  }
}
