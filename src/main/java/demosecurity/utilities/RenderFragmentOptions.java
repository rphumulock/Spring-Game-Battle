package demosecurity.utilities;
public class RenderFragmentOptions {
    private QuerySelector querySelector;
    private FragmentMergeType merge;
    private long settleDuration;

    public RenderFragmentOptions(QuerySelector querySelector, FragmentMergeType merge, long settleDuration) {
        this.querySelector = querySelector;
        this.merge = merge;
        this.settleDuration = settleDuration;
    }

    public QuerySelector getQuerySelector() {
        return querySelector;
    }

    public FragmentMergeType getMerge() {
        return merge;
    }

    public long getSettleDuration() {
        return settleDuration;
    }

    public void setQuerySelector(QuerySelector querySelector) {
        this.querySelector = querySelector;
    }

    public void setMerge(FragmentMergeType merge) {
        this.merge = merge;
    }

    public void setSettleDuration(long settleDuration) {
        this.settleDuration = settleDuration;
    }

}
