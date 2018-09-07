package it.a4smart.vateoperator;

import java.util.Objects;

public class InstallableBeacon {
    private String identifier;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean identifierSetted() {
        return identifier != null;
    }

    public boolean equalsIdentifier(String identifier) {
        return this.identifier.equals(identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstallableBeacon that = (InstallableBeacon) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
