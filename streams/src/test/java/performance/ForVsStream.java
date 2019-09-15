package performance;

import compare.FilterCompare;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@State(value = Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ForVsStream {
    private FilterCompare filter;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(ForVsStream.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        filter = new FilterCompare(true);
    }

    @Benchmark
    public int filterArrayFor() {
        return filter.filterArrayFor();
    }


    @Benchmark
    public int filterArrayStream() {
        return filter.filterArrayStream();
    }


    @Benchmark
    public int filterFor() {
        return filter.filterFor();
    }

    @Benchmark
    public int filterStream() {
        return filter.filterStream();
    }

    @Benchmark
    public int filterStreamUnordered() {
        return filter.filterStreamUnordered();
    }

    @Benchmark
    public int filterStreamParallel() throws ExecutionException, InterruptedException {
        return filter.filterStreamParallel();
    }

/*
int size = 100_000;

Benchmark                          Mode  Cnt  Score   Error  Units
ForVsStream.filterArrayFor           ss       2.910          ms/op
ForVsStream.filterFor                ss       4.325          ms/op

ForVsStream.filterArrayStream        ss       3.656          ms/op
ForVsStream.filterStream             ss       3.403          ms/op
ForVsStream.filterStreamParallel     ss       6.813          ms/op
ForVsStream.filterStreamUnordered    ss       3.490          ms/op

with file loading:
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       34.249          ms/op
ForVsStream.filterFor                ss       39.893          ms/op

ForVsStream.filterArrayStream        ss       33.266          ms/op
ForVsStream.filterStream             ss       32.713          ms/op
ForVsStream.filterStreamParallel     ss       43.345          ms/op
ForVsStream.filterStreamUnordered    ss       31.935          ms/op


int size = 200_000;
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss        3.952          ms/op
ForVsStream.filterFor                ss        6.486          ms/op

ForVsStream.filterArrayStream        ss        4.972          ms/op
ForVsStream.filterStream             ss        4.404          ms/op
ForVsStream.filterStreamParallel     ss       15.560          ms/op
ForVsStream.filterStreamUnordered    ss        6.879          ms/op

with file loading:
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       48.315          ms/op
ForVsStream.filterFor                ss       48.908          ms/op

ForVsStream.filterArrayStream        ss       46.101          ms/op
ForVsStream.filterStream             ss       43.475          ms/op
ForVsStream.filterStreamParallel     ss       43.912          ms/op
ForVsStream.filterStreamUnordered    ss       40.604          ms/op

int size = 500_000;
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       11.363          ms/op
ForVsStream.filterFor                ss       12.393          ms/op
ForVsStream.filterArrayStream        ss        9.089          ms/op
ForVsStream.filterStream             ss        8.991          ms/op
ForVsStream.filterStreamParallel     ss       20.155          ms/op
ForVsStream.filterStreamUnordered    ss        9.049          ms/op

with file loading:
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       52.841          ms/op
ForVsStream.filterFor                ss       48.122          ms/op
ForVsStream.filterArrayStream        ss       39.541          ms/op
ForVsStream.filterStream             ss       36.621          ms/op
ForVsStream.filterStreamParallel     ss       43.384          ms/op
ForVsStream.filterStreamUnordered    ss       37.716          ms/op

int size = 1_000_000;
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       12.432          ms/op
ForVsStream.filterFor                ss       15.527          ms/op
ForVsStream.filterArrayStream        ss       12.929          ms/op
ForVsStream.filterStream             ss       13.588          ms/op
ForVsStream.filterStreamParallel     ss       25.786          ms/op
ForVsStream.filterStreamUnordered    ss       12.858          ms/op

with file loading:
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       58.055          ms/op
ForVsStream.filterFor                ss       55.326          ms/op
ForVsStream.filterArrayStream        ss       50.167          ms/op
ForVsStream.filterStream             ss       46.467          ms/op
ForVsStream.filterStreamParallel     ss       55.828          ms/op
ForVsStream.filterStreamUnordered    ss       54.835          ms/op


int size = 10_000_000;
Benchmark                          Mode  Cnt   Score   Error  Units
ForVsStream.filterArrayFor           ss       69.356          ms/op
ForVsStream.filterFor                ss       79.114          ms/op
ForVsStream.filterArrayStream        ss       93.983          ms/op
ForVsStream.filterStream             ss       93.268          ms/op
ForVsStream.filterStreamParallel     ss       47.363          ms/op
ForVsStream.filterStreamUnordered    ss       94.165          ms/op

with file loading:
Benchmark                          Mode  Cnt    Score   Error  Units
ForVsStream.filterArrayFor           ss        91.756          ms/op
ForVsStream.filterFor                ss       106.619          ms/op
ForVsStream.filterArrayStream        ss       124.710          ms/op
ForVsStream.filterStream             ss       117.754          ms/op
ForVsStream.filterStreamParallel     ss        76.115          ms/op
ForVsStream.filterStreamUnordered    ss       118.851          ms/op

int size = 100_000_000;
Benchmark                          Mode  Cnt    Score   Error  Units
ForVsStream.filterArrayFor           ss       670.561          ms/op
ForVsStream.filterFor                ss       790.759          ms/op
ForVsStream.filterArrayStream        ss       991.561          ms/op
ForVsStream.filterStream             ss       967.014          ms/op
ForVsStream.filterStreamParallel     ss       262.555          ms/op
ForVsStream.filterStreamUnordered    ss       960.990          ms/op


Benchmark                          Mode  Cnt     Score   Error  Units
ForVsStream.filterArrayFor           ss        818.125          ms/op
ForVsStream.filterFor                ss        857.030          ms/op
ForVsStream.filterArrayStream        ss       1031.291          ms/op
ForVsStream.filterStream             ss        998.899          ms/op
ForVsStream.filterStreamParallel     ss        296.794          ms/op
ForVsStream.filterStreamUnordered    ss       1000.393          ms/op
*/
}
