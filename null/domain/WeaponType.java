package null.domain;

public enum WeaponType {

	ADAF("adaf"),
	SFDSDFFSFSF("sfdsdf  fsfsf"),
	ATOMBOMBA("atom bomba"),
    ;

    private String displayName;

    private WeaponType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}