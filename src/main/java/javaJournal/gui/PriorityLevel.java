package javaJournal.gui;

import java.awt.Color;

public enum PriorityLevel {
    EMERGENCY("color:#FF0000;font-weight:bold"),
    ALERT("color:#EE0000;font-weight:bold"),
    CRITICAL("color:#DD0000;font-weight:bold"),
    ERROR("color:#CC0000;font-weight:bold"),
    WARNING("color:#D4AC0D"),
    NOTICE("font-weight:bold"),
    INFO(""),
    DEBUG("color:#888888"),
    MISSING("color:#1F618D");
    
    public final String messageStyle;
    
    private PriorityLevel(String ms) {
        messageStyle = ms;
    }
}
