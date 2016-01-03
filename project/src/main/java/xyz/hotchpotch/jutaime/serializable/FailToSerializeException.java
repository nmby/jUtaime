package xyz.hotchpotch.jutaime.serializable;

/**
 * シリアライズに失敗したことを表す実行時例外です。<br>
 * 失敗の原因となった例外を cause として保持します。<br>
 * 
 * @since 1.3.0
 * @author nmby
 */
public class FailToSerializeException extends RuntimeException {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * シリアライズに失敗したことを表す例外を生成します。<br>
     * 
     * @param cause 失敗の原因となった例外またはエラー
     */
    public FailToSerializeException(Throwable cause) {
        super(cause);
    }
}
