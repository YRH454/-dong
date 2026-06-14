<%@ page pageEncoding="UTF-8" import="java.io.*"%>
<%!
synchronized String rwFile(String filepath) {
  File f = new File(filepath);
  try {
    FileInputStream fi = new FileInputStream(f);
    DataInputStream di = new DataInputStream(fi);
    long i = di.readLong(); fi.close(); di.close();
    i++;
    FileOutputStream fo = new FileOutputStream(f);
    DataOutputStream dos = new DataOutputStream(fo);
    dos.writeLong(i); fo.close(); dos.close();
    String s1 = i + "";
    StringBuilder s2 = new StringBuilder();
    for (int k = 0; k < s1.length(); k++) {
      s2.append("<img src='images/").append(s1.charAt(k)).append(".gif' alt='").append(s1.charAt(k)).append("'>");
    }
    return s2.toString();
  } catch (FileNotFoundException e1) {
    try {
      FileOutputStream fo = new FileOutputStream(f);
      DataOutputStream dos = new DataOutputStream(fo);
      dos.writeLong(1); fo.close(); dos.close();
    } catch (Exception e) {}
    return "<img src='images/1.gif' alt='1'>";
  } catch (IOException e2) {
    return "";
  }
}

synchronized String rFile(String filepath) {
  File f = new File(filepath);
  try {
    FileInputStream fi = new FileInputStream(f);
    DataInputStream di = new DataInputStream(fi);
    long i = di.readLong(); fi.close(); di.close();
    String s1 = i + "";
    StringBuilder s2 = new StringBuilder();
    for (int k = 0; k < s1.length(); k++) {
      s2.append("<img src='images/").append(s1.charAt(k)).append(".gif' alt='").append(s1.charAt(k)).append("'>");
    }
    return s2.toString();
  } catch (FileNotFoundException e1) {
    try {
      FileOutputStream fo = new FileOutputStream(f);
      DataOutputStream dos = new DataOutputStream(fo);
      dos.writeLong(1); fo.close(); dos.close();
    } catch (Exception e) {}
    return "<img src='images/1.gif' alt='1'>";
  } catch (IOException e2) {
    return "";
  }
}
%>
<%
if (session.isNew()) {
  String n = this.rwFile(application.getRealPath("/") + "counter.txt");
  out.print("您是第 " + n + " 位访问者");
} else {
  String n = this.rFile(application.getRealPath("/") + "counter.txt");
  out.print("您是第 " + n + " 位访问者");
}
%>
