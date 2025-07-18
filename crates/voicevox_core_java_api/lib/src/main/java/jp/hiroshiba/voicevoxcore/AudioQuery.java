package jp.hiroshiba.voicevoxcore;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import jp.hiroshiba.voicevoxcore.internal.Dll;

/**
 * AudioQuery（音声合成用のクエリ）。
 *
 * <p>JSONの形式はVOICEVOX ENGINEと同じになっている。ただし今後の破壊的変更にて変わる可能性がある。<a
 * href="https://github.com/VOICEVOX/voicevox_core/blob/main/docs/guide/user/serialization.md"
 * target="_blank">データのシリアライゼーション</a>を参照。
 *
 * <p>現在この型はGSONに対応しているが、将来的には <a href="https://github.com/VOICEVOX/voicevox_core/issues/984"
 * target="_blank">Jacksonに切り替わる予定</a> 。
 */
public class AudioQuery {
  static {
    Dll.loadLibrary();
  }

  /** アクセント句の配列。 */
  @SerializedName("accent_phrases")
  @Expose
  @Nonnull
  public List<AccentPhrase> accentPhrases;

  /** 全体の話速。 */
  @Expose public double speedScale;

  /** 全体の音高。 */
  @Expose public double pitchScale;

  /** 全体の抑揚。 */
  @Expose public double intonationScale;

  /** 全体の音量。 */
  @Expose public double volumeScale;

  /** 音声の前の無音時間。 */
  @Expose public double prePhonemeLength;

  /** 音声の後の無音時間。 */
  @Expose public double postPhonemeLength;

  /** 音声データの出力サンプリングレート。 */
  @Expose public int outputSamplingRate;

  /** 音声データをステレオ出力するか否か。 */
  @Expose public boolean outputStereo;

  /**
   * [読み取り専用] AquesTalk風記法。
   *
   * <p>{@link jp.hiroshiba.voicevoxcore.blocking.Synthesizer#createAudioQuery} が返すもののみ String
   * となる。入力としてのAudioQueryでは無視される。
   */
  @Expose @Nullable public final String kana;

  public AudioQuery() {
    this.accentPhrases = new ArrayList<>();
    this.speedScale = 1.0;
    this.pitchScale = 0.0;
    this.intonationScale = 1.0;
    this.volumeScale = 1.0;
    this.prePhonemeLength = 0.1;
    this.postPhonemeLength = 0.1;
    this.outputSamplingRate = 24000;
    this.kana = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AudioQuery)) {
      return false;
    }
    AudioQuery other = (AudioQuery) obj;
    return accentPhrases.equals(other.accentPhrases)
        && speedScale == other.speedScale
        && pitchScale == other.pitchScale
        && intonationScale == other.intonationScale
        && volumeScale == other.volumeScale
        && prePhonemeLength == other.prePhonemeLength
        && postPhonemeLength == other.postPhonemeLength
        && outputSamplingRate == other.outputSamplingRate
        && outputStereo == other.outputStereo;
  }

  public static AudioQuery fromAccentPhrases(List<AccentPhrase> accentPhrases) {
    Gson gson = new Gson();
    String queryJson = rsFromAccentPhrases(gson.toJson(accentPhrases));
    AudioQuery query = gson.fromJson(queryJson, AudioQuery.class);
    if (query == null) {
      throw new NullPointerException();
    }
    return query;
  }

  @Nonnull
  private static native String rsFromAccentPhrases(String accentPhrases);
}
