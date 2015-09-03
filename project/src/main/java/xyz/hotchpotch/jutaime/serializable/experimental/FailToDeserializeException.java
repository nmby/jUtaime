package xyz.hotchpotch.jutaime.serializable.experimental;

/**
 * デシリアル化に失敗したことを表す実行時例外です。<br>
 * 失敗の原因となった例外を cause として保持します。<br>
 * 
 * @since 
 * @author nmby
 */
public class FailToDeserializeException extends RuntimeException {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    public FailToDeserializeException(Throwable cause) {
        super(cause);
    }
}
