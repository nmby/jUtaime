package xyz.hotchpotch.jutaime.serializable;

/**
 * デシリアライズに失敗したことを表す実行時例外です。<br>
 * 失敗の原因となった例外を cause として保持します。<br>
 * 
 * @since 1.3.0
 * @author nmby
 */
public class FailToDeserializeException extends RuntimeException {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * デシリアライズに失敗したことを表す例外を生成します。<br>
     * 
     * @param cause 失敗の原因となった例外またはエラー
     */
    public FailToDeserializeException(Throwable cause) {
        super(cause);
    }
}
