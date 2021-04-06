package P1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MagicSquare {
	public boolean isLegalMagicSquare(String filename) { 
		File file=new File(filename);
		boolean excflag=true;//是否出现异常情况的标志
		boolean posflag=true;//是否出现了负数
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(file));
			String templine=null;
			int linenum=0;//记录行号
			templine=reader.readLine();
			String[] firstline=templine.split("\t");
			int length=firstline.length;
			int []linesum=new int[length];//存储行和的数组
			int []colsum=new int[length];//存储列和的数组
			int []diagonal=new int[2];//存储两个对角线和的数组
			while(templine!=null) { //逐行读取文件
				String[] line=templine.split("\t");//按'\t'进行划分
				int linelen=line.length;
				if(linelen==length) {
				int i=0;//记录读取到该行的第几个元素
				while(i<linelen) {
					if(linenum>=length) {//行数与列数不同，为特殊情况
						excflag=false;
						break;
					}
					Integer tmp=0;
					try{
						tmp=Integer.valueOf(line[i]);
					}catch (NumberFormatException e1) {
						System.out.print("The data is not organized as standardize matrix!Please check '\\t' or the float number.  ");
						reader.close();
						return false;
					}
					if(tmp<=0) {
						posflag=false;
						break;
					}
					linesum[linenum]+=tmp;//行和
					colsum[i]+=tmp;//列和
					if(i==linenum) {//主对角线元素加和
						diagonal[0]+=tmp;
					}
					if((i+linenum)==length-1) {//逆对角线元素加和
						diagonal[1]+=tmp;
					}
					i++;
				}
				}
				else {//该行与其他行元素个数不同，为特殊情况
					excflag=false;
				}
				if(excflag==false) {//输入符合特殊情况，直接退出循环
					break;
				}
				templine=reader.readLine();
				linenum++;
			}
			reader.close();
			if(excflag==false) {
				System.out.print("The number of the line and the column of matrix is not equal!  ");
				return false;
			}
			if(posflag==false) {
				System.out.print("There is a negative number in the matrix!  ");
				return false;
			}
			if(diagonal[0]!=diagonal[1]) {//主对角线、逆对角线元素和不同
				return false;
			}
			else if(diagonal[0]!=linesum[0]) {//行和与主对角线和不同
				return false;
			}
			for(int i=1;i<length;i++) {
				if(linesum[i]!=linesum[i-1]) {//不同行和不同
					return false;
				}
				if(colsum[i]!=colsum[i-1]) {//不同列和不同
					return false;
				}
				if(linesum[i]!=colsum[i]) {//行、列和不同
					return false;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(reader!=null) {
				try {
					reader.close();
				}catch (IOException e1) {
				}
			}
		}
		return true;
	}
	
	public static boolean generateMagicSquare(int n) {
		int magic[][]=new int[n][n];//声明一个n*n矩阵
		if((n%2==0)||(n<0)) {//当n为偶数或负数时提示错误并返回
			System.out.println("The 'n' is not suit!Please input again!");
			return false;
		}
		int row=0,col=n/2,i,j,square=n*n;
		try {
			File file=new File("src/P1/txt/6.txt");
			FileWriter writefile=new FileWriter(file);
			BufferedWriter exm=new BufferedWriter(writefile);
		for(i=1;i<=square;i++) {//矩阵元素的值在1至n^2之间
			magic[row][col]=i;//首先将1置于第0行的中间
			if(i%n==0)//当下一个元素位置已经有整数或者位置已经到达右上角顶点
				row++;//则选择其正下方位置做替代
			else {//整体上沿自左下至右上的对角线方向放置这些连续正整数
				if(row==0)//当回到第0行时，下一个正整数要置于最下一行
					row=n-1;
				else //否则行数不断减少，实现在由下到上的方向放置
					row--;
				if(col==(n-1))//如果到达最右列，则下一次要回到最左列
					col=0;
				else col++;//否则列数不断增加，实现在由左到右方向上放置
			}//由下到上，由左到右，整体上便是由左下到右上
		}
		for(i=0;i<n;i++) {//将生成的矩阵写入文件
			for(j=0;j<n;j++) {
				exm.write(magic[i][j]+"\t");
			}
			exm.write("\n");
		}
		exm.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static void main(String[] args) {
		MagicSquare ms=new MagicSquare();
		System.out.print("1.txt: ");
		boolean flag=ms.isLegalMagicSquare("./src/P1/txt/1.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
		System.out.print("2.txt: ");
		flag=ms.isLegalMagicSquare("./src/P1/txt/2.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
		System.out.print("3.txt: ");
		flag=ms.isLegalMagicSquare("./src/P1/txt/3.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
		System.out.print("4.txt: ");
		flag=ms.isLegalMagicSquare("./src/P1/txt/4.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
		System.out.print("5.txt: ");
		flag=ms.isLegalMagicSquare("./src/P1/txt/5.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
		System.out.print("6.txt: ");
		flag=ms.isLegalMagicSquare("./src/P1/txt/6.txt");
		if(flag==true) {
			System.out.println("It's a MagicSquare!");
		}
		else {
			System.out.println("It's not a MagicSquare!");
		}
	}
}
