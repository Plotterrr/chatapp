package edu.rice.comp504.model.cmd;

/**
 * A factory that makes ICmd.
 */
public interface ICmdFac {
    /**
     * Make a cmd based on type.
     * @return ICmd
     */
    ICmd make(String type);
}
