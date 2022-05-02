public class PreReq {
    private int code;
    private int preCode;
    private String dept;
    private String preDept;


    public PreReq(int c, String d, int pc, String pd)
    {
        setCode(c);
        setDept(d);
        setPreCode(pc);
        setPreDept(pd);
    }


    public String getDept() {
        return dept;
    }


    public void setDept(String dept) {
        this.dept = dept;
    }


    public String getPreDept() {
        return preDept;
    }


    public void setPreDept(String preDept) {
        this.preDept = preDept;
    }


    public int getPreCode() {
        return preCode;
    }


    public void setPreCode(int preCode) {
        this.preCode = preCode;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }
    
}
