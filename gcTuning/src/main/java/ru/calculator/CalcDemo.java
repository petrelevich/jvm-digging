package ru.calculator;


/*
-Xms1024m
-Xmx1024m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m

 */

/*
jps
jinfo -flag ConcGCThreads <pid>
jinfo -flag ParallelGCThreads <pid>
jinfo -flag MaxGCPauseMillis <pid>
jinfo -flag NewRatio <pid>
*/

/*
-XX:MaxGCPauseMillis=10
-XX:MaxGCPauseMillis=1000

-XX:NewRatio=2
-XX:MaxTenuringThreshold=2

-XX:ConcGCThreads=10
-XX:ParallelGCThreads=10

 */


import java.time.LocalDateTime;

public class CalcDemo {
    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
