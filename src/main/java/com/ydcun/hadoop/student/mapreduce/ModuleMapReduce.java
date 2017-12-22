package com.ydcun.hadoop.student.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 *	自己实现一个 单词统计MapReduce
 * @author ydcun-pro
 *
 */
public class ModuleMapReduce extends Configured implements Tool{
	/**
	 * 继承mapper类重写map方法
	 */
	//TODO
	public static class ModuleMapper
		extends Mapper<LongWritable, Text, Text, IntWritable>{
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			//Nothing
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//TODO
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			//Noting
		}
	}
	/**
	 * 继承reducer 重写reduce方法
	 */
	//TODO
	public static class ModuleReducer
		extends Reducer<Text, IntWritable, Text, IntWritable>{
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			// Nothing
		}

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
							  Context context) throws IOException, InterruptedException {
			// TODO

		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			// Nothing
		}
	}
	//drive
	public int run(String args[]) throws Exception{
		//1.load config
		Configuration conf = this.getConf();
		//2.create job
		Job job = Job.getInstance(conf, this.getClass().getName());
		job.setJarByClass(this.getClass());
		
		//3.1 input
		Path inputPath = new Path(args[0]);
		FileInputFormat.setInputPaths(job, inputPath);
		
		//3.2 set mapper
		job.setMapperClass(ModuleMapper.class);
		//TODO
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		//3.3 set reducer
		job.setReducerClass(ModuleReducer.class);
		//TODO
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		//3.4 output
		Path outputPath = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		//4. job submit
		boolean isSuccess = job.waitForCompletion(true);

		return isSuccess?0:1;
	}

	public static void main(String[] args) throws Exception {
		// TODO
		Configuration configuration = new Configuration();
		int isSuccess = ToolRunner.run(configuration,new ModuleMapReduce(),args);
		System.exit(isSuccess);
	}

}
