call createUserStraf.cmd
imp USERID=feelstraf/feelstraf@TESTSYSDB BUFFER=2048 TOUSER=feelstraf  fromuser=feelstraf file=testsys.dmp log=feelstraf_xl.log
pause