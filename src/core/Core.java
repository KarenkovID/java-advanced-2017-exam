package core;

import com.sun.istack.internal.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * String manager which provides remote access to string base
 */
public interface Core extends Remote{
    /**
     * Add string to string manager
     * @param str is string to add
     * @throws RemoteException if was error during remote accessing
     */
    void addString(String str) throws RemoteException;

    /**
     * Remove given string from manager
     * @param str string to remove
     * @throws RemoteException if was error during remote accessing
     */
    void removeString(String str) throws RemoteException;

    /**
     * Return all string in manager
     * @return all string in manager
     * @throws RemoteException if was error during remote accessing
     */
    @NotNull
    List<String> getAllStrings() throws RemoteException;

    /**
     * Check given string for including in manager
     * @param strings list for matching
     * @return boolean array. res[i] = 1, if strings[i] includes in manager
     * @throws RemoteException if was error during remote accessing
     */
    boolean[] includes(List<String> strings) throws RemoteException;

    /**
     * Return list contains of {@link String} that contains info about last command in format :
     * method_name + " " + method_arg (if exist)
     * @return last request list
     * @throws RemoteException if was error during remote accessing
     */
    List<String> getLastRequests() throws RemoteException;
}
