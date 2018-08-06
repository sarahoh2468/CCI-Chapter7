import com.sun.corba.se.impl.orbutil.graph.Node;

import java.util.LinkedList;

public class CallCenter {
    //Example
    private int countR = 0;
    private int countM = 0;
    private int countD = 0;
    public LinkedList<Employee> respondents;
    public LinkedList<Employee> managers;
    public LinkedList<Employee> directors;
    public class Employee{
        private String[] types = {"respondent", "manager", "director"};
        private String type;
        private boolean ongoingCall;

        public Employee(String t) {
            this.ongoingCall = false;
            setType(t);
            if (t.equals(types[0])) {
                respondents.add(this);
                countR++;
            }
            else if (t.equals(types[1])) {
                managers.add(this);
                countM++;
            }
            else {
                directors.add(this);
                countD++;
            }
        }

        public void setType(String t) {
            if (isValidType(t)) {
                this.type = t;
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        public boolean isValidType(String t) {
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(t)) {
                    return true;
                }
            }
            return false;
        }


    }

    public class Call{
        public Call() {
            dispatchCall();
        }
        public String dispatchCall() {
            if (countR > 0) {
                countR--;
                for (Employee n: respondents) {
                    if (n.ongoingCall == false) {
                        n.ongoingCall = true;
                        return "respondent";
                    }
                }
            }
            if (countM > 0) {
                countM--;
                for (Employee n: managers) {
                    if (n.ongoingCall == false) {
                        n.ongoingCall = true;
                        return "manager"
                    }
                }
            }
            if (countD > 0) {
                countD--;
                for (Employee n: directors) {
                    if (n.ongoingCall == false) {
                        n.ongoingCall = true;
                        return "director";
                    }
                }
            }
            return "please call again";
        }
        public void endCall(Employee e) {
            if (e.type.equals("respondent")) {
                for (Employee n: respondents) {
                    if (n.ongoingCall == true) {
                        n.ongoingCall = false;
                    }
                }
            }
            if (e.type.equals("manager")) {
                for (Employee n: managers) {
                    if (n.ongoingCall == true) {
                        n.ongoingCall = false;
                    }
                }
            }
            if (e.type.equals("director")) {
                for (Employee n: respondents) {
                    if (n.ongoingCall == true) {
                        n.ongoingCall = false;
                    }
                }
            }
        }

    }
}
