package vn.com.basc.inco.common;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by SONY on 6/8/2016.
 */
public class TicketForm {
    public LinkedHashMap<String,String> deparments;
    public LinkedHashMap<String,String> ticketsStatus;
    public LinkedHashMap<String,String> ticketsTypes;
    public String  ticketsTypesDefault;
    public String ticketsStatusDefault;
    public LinkedHashMap<String,LinkedHashMap<String,String>> users;
    public LinkedHashMap<String,LinkedHashMap<String,String>> notify;
}
