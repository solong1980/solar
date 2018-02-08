#bash
pid=`ps -aux|grep java|grep solar|awk '{print $2}'`
echo $pid
if [ $pid == '' ]; then
	echo 'no solar proccess'
	cp=''
	
	for l in `ls ./solar -l | awk '/jar$/{print $NF}'`
	do
		cp=${cp}${l}:
	done
	
	echo $cp
	
	java -cp $cp com.solar.server.mina.bootstrap.SolarServer
fi

