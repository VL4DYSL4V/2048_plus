package command;

public interface VolatileCommand<T> extends Command {

    void setParam(T param);

}
