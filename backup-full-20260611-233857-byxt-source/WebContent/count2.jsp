<%@ page pageEncoding="gbk" import="java.io.*"%>
<html>
<head>
<title>网页计数器</title>
</head>
<body>
<%!
synchronized String rwFile(String filepath){
File f=new File(filepath);
try
       {
        FileInputStream fi=new FileInputStream(f);
        DataInputStream di=new DataInputStream(fi);
        long  i=di.readLong(); fi.close();di.close();        
        i++;
        
        FileOutputStream fo=new FileOutputStream(f);
        DataOutputStream dos=new   
            DataOutputStream(fo);
        dos.writeLong(i);fo.close();dos.close();
        String s1=i+"";
        String s2="";
        for(int k=0;k<s1.length();k++){
         s2=s2+"<img src=images/"+s1.charAt(k)+".gif"+">";
        }
       
        return s2;
}catch(FileNotFoundException e1){
      try{ FileOutputStream fo=new FileOutputStream(f);
            DataOutputStream dos=new   
            DataOutputStream(fo);
             dos.writeLong(1);fo.close();dos.close();
         
         }catch(Exception e){}
      return "<img src=images/1.gif>";
}catch(IOException e2){
  System.out.print(e2.toString());
  return "";
}
}
%>
<%!
synchronized String rFile(String filepath){
File f=new File(filepath);
try
       {
        FileInputStream fi=new FileInputStream(f);
        DataInputStream di=new DataInputStream(fi);
        long  i=di.readLong(); fi.close();di.close();        
        String s1=i+"";
        String s2="";
        for(int k=0;k<s1.length();k++){
         s2=s2+"<img src=images/"+s1.charAt(k)+".gif"+">";
        }
       
        return s2;
}catch(FileNotFoundException e1){
      try{ FileOutputStream fo=new FileOutputStream(f);
            DataOutputStream dos=new   
            DataOutputStream(fo);
             dos.writeLong(1);fo.close();dos.close();
         
         }catch(Exception e){}
      return "<img src=images/1.gif>";
}catch(IOException e2){
  System.out.print(e2.toString());
  return "";
}
}
 %>

<%

if(session.isNew()){
	String n=this.rwFile(application.getRealPath("/")+"counter.txt");
	  out.print("您是第"+n+"位访问者");
}
else{
	String n=this.rFile(application.getRealPath("/")+"counter.txt");
	  out.print("您是第"+n+"位访问者");
}
 

 %>
</body>
</html>