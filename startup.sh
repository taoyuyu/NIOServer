#/bin/sh
CLASSPATH=./NIOServer-1.0-SNAPSHOT.jar:./../conf

for i in ../lib/*.jar;do 
    CLASSPATH=$CLASSPATH:$i
done

export CLASSPATH=:$CLASSPATH

exec java -classpath .:${CLASSPATH} com.whu.yves.main.Main

#exec java -classpath NIOServer-1.0-SNAPSHOT.jar com.whu.yves.main.Main
