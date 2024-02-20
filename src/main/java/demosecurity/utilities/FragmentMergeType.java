package demosecurity.utilities;

public enum FragmentMergeType {
    FragmentMergeMorphElement("morph_element"),
    FragmentMergeInnerElement("inner_element"),
    FragmentMergeOuterElement("outer_element"),
    FragmentMergePrependElement("prepend_element"),
    FragmentMergeAppendElement("append_element"),
    FragmentMergeBeforeElement("before_element"),
    FragmentMergeAfterElement("after_element"),
    FragmentMergeDeleteElement("delete_element"),
    FragmentMergeUpsertAttributes("upsert_attributes");

    private final String value;

    FragmentMergeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}