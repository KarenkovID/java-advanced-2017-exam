package core;

import com.sun.istack.internal.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by karen on 01.06.2017.
 */
public interface Core extends Remote{
    void addString(String str) throws RemoteException;

    void removeString(String str) throws RemoteException;

    @NotNull
    List<String> getAllStrings() throws RemoteException;

    boolean[] includes(List<String> strings) throws RemoteException;

    List<String> getLastRequests() throws RemoteException;
}
