chisel test
参考`core/src/example.scala`, `core/test/example.scala`
在workbench目录下
```bash
sbt "run example.ExampleTestMain"
```
example是package名，ExampleTestMain是Main Object

## 如何写不带跳转的minitest
在`minitest/src`下写`$(cmd).S`
```assembly
    .globl _start
_start:
    xxx
    xxx
    xxx
```
xxx为测试指令
在`minitest`目录下`make run-$(cmd)`，`cmd`对应测试名