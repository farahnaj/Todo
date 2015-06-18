package mytodo

class Todo {
    String title;
    Date due_date;
    boolean is_active = true;

    static constraints = {
    }

    static mapping = {
        table "Todo"
        sort "due_date"
    }
}