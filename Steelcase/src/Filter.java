
public class Filter {
    private String value = "";
    private String param = "";

    private String acceptedParams[] =  {"professor", "name", "description", "code", "department", "section"};

    protected Filter() {}

    public Filter(String param, String value) {
        this.param = param;
        this.value = value;
    }

    public boolean isValidParam(String s)
    {
        for(int i = 0; i < acceptedParams.length; i++)
        {
            if(s.equals(acceptedParams[i]))
            {
                
                return true;
            }
        }
        return false;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String s)
    {
        value = s;
    }

    public String getParam()
    {
        return param;
    }

    public void setParam(String p)
    {
        param = p;
    }

    public String toString()
    {
        return param;
    }

    public String[] getAcceptedParams() {
        return this.acceptedParams;
    }
}
