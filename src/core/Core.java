package core;

import com.sun.istack.internal.NotNull;

import java.rmi.Remote;
import java.util.List;

/**
 * Created by karen on 01.06.2017.
 */
public interface Core extends Remote{
    void addString(String str);

    void removeString(String str);

    @NotNull
    List<String> getAllStrings();

    List<String> includes(List<String> strings);

    List<String> getLastRequests();
}
