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
		boolean excflag=true;//�Ƿ�����쳣����ı�־
		boolean posflag=true;//�Ƿ�����˸���
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(file));
			String templine=null;
			int linenum=0;//��¼�к�
			templine=reader.readLine();
			String[] firstline=templine.split("\t");
			int length=firstline.length;
			int []linesum=new int[length];//�洢�к͵�����
			int []colsum=new int[length];//�洢�к͵�����
			int []diagonal=new int[2];//�洢�����Խ��ߺ͵�����
			while(templine!=null) { //���ж�ȡ�ļ�
				String[] line=templine.split("\t");//��'\t'���л���
				int linelen=line.length;
				if(linelen==length) {
				int i=0;//��¼��ȡ�����еĵڼ���Ԫ��
				while(i<linelen) {
					if(linenum>=length) {//������������ͬ��Ϊ�������
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
					linesum[linenum]+=tmp;//�к�
					colsum[i]+=tmp;//�к�
					if(i==linenum) {//���Խ���Ԫ�ؼӺ�
						diagonal[0]+=tmp;
					}
					if((i+linenum)==length-1) {//��Խ���Ԫ�ؼӺ�
						diagonal[1]+=tmp;
					}
					i++;
				}
				}
				else {//������������Ԫ�ظ�����ͬ��Ϊ�������
					excflag=false;
				}
				if(excflag==false) {//����������������ֱ���˳�ѭ��
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
			if(diagonal[0]!=diagonal[1]) {//���Խ��ߡ���Խ���Ԫ�غͲ�ͬ
				return false;
			}
			else if(diagonal[0]!=linesum[0]) {//�к������Խ��ߺͲ�ͬ
				return false;
			}
			for(int i=1;i<length;i++) {
				if(linesum[i]!=linesum[i-1]) {//��ͬ�кͲ�ͬ
					return false;
				}
				if(colsum[i]!=colsum[i-1]) {//��ͬ�кͲ�ͬ
					return false;
				}
				if(linesum[i]!=colsum[i]) {//�С��кͲ�ͬ
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
		int magic[][]=new int[n][n];//����һ��n*n����
		if((n%2==0)||(n<0)) {//��nΪż������ʱ��ʾ���󲢷���
			System.out.println("The 'n' is not suit!Please input again!");
			return false;
		}
		int row=0,col=n/2,i,j,square=n*n;
		try {
			File file=new File("src/P1/txt/6.txt");
			FileWriter writefile=new FileWriter(file);
			BufferedWriter exm=new BufferedWriter(writefile);
		for(i=1;i<=square;i++) {//����Ԫ�ص�ֵ��1��n^2֮��
			magic[row][col]=i;//���Ƚ�1���ڵ�0�е��м�
			if(i%n==0)//����һ��Ԫ��λ���Ѿ�����������λ���Ѿ��������ϽǶ���
				row++;//��ѡ�������·�λ�������
			else {//�������������������ϵĶԽ��߷��������Щ����������
				if(row==0)//���ص���0��ʱ����һ��������Ҫ��������һ��
					row=n-1;
				else //�����������ϼ��٣�ʵ�������µ��ϵķ������
					row--;
				if(col==(n-1))//������������У�����һ��Ҫ�ص�������
					col=0;
				else col++;//���������������ӣ�ʵ���������ҷ����Ϸ���
			}//���µ��ϣ������ң������ϱ��������µ�����
		}
		for(i=0;i<n;i++) {//�����ɵľ���д���ļ�
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
