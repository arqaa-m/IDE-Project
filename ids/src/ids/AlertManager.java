package ids;

import java.util.ArrayList;
import java.util.Stack;

public class AlertManager {

    public static final String RULE_BRUTE_FORCE = "BRUTE_FORCE_DETECTION";
    public static final String RULE_PORT_SCAN   = "PORT_SCAN_DETECTION";

    private Stack<String>     ruleStack;
    private ArrayList<String> alertLog;

    public AlertManager() {
        ruleStack = new Stack<>();
        alertLog  = new ArrayList<>();
    }

    public void pushRule(String rule) {
        if (!ruleStack.contains(rule)) {
            ruleStack.push(rule);
        }
    }

    public String popRule() {
        if (ruleStack.isEmpty()) return null;
        return ruleStack.pop();
    }

    public void removeRule(String rule) {
        ruleStack.remove(rule);
    }

    public boolean isRuleActive(String rule) {
        return ruleStack.contains(rule);
    }

    public boolean fireAlert(String rule, String alertText) {
        if (!isRuleActive(rule)) return false;
        alertLog.add(alertText);
        System.out.println("  *** ALERT: " + alertText);
        return true;
    }

    public void clearAlerts() {
        alertLog.clear();
    }

    public ArrayList<String> getAlertLog()  { return alertLog; }
    public Stack<String>     getRuleStack() { return ruleStack; }
    public int               alertCount()  { return alertLog.size(); }

    public void displayActiveRules() {
        System.out.println("  Active rules: " + ruleStack.size());
        if (ruleStack.isEmpty()) {
            System.out.println("  (all detection disabled)");
        } else {
            for (String r : ruleStack) {
                System.out.println("    [ON] " + r);
            }
        }
    }

    public void displayAlertLog() {
        System.out.println("  Total alerts: " + alertLog.size());
        if (alertLog.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        for (int i = 0; i < alertLog.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + alertLog.get(i));
        }
    }

    public void reset() {
        ruleStack.clear();
        alertLog.clear();
    }
}