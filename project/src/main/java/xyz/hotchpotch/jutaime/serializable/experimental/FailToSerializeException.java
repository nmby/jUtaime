package xyz.hotchpotch.jutaime.serializable.experimental;

/**
 * シリアライズに失敗したことを表す実行時例外です。<br>
 * 失敗の原因となった例外を cause として保持します。<br>
 * 
 * @deprecated このクラスは {@link xyz.hotchpotch.jutaime.serializable.FailToSerializeException} として正式リリースされました。<br>
 *             今後は {@link xyz.hotchpotch.jutaime.serializable.experimental} パッケージではなく
 *             {@link xyz.hotchpotch.jutaime.serializable} パッケージの各種クラスを利用してください。<br>
 *             このパッケージは将来のリリースで削除される予定です。<br>
 * @since 1.2.0
 * @author nmby
 */
@Deprecated
public class FailToSerializeException extends RuntimeException {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    public FailToSerializeException(Throwable cause) {
        super(cause);
    }
}
