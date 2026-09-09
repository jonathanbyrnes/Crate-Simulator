package core.craft.openingservice.service;

@FunctionalInterface
public interface Randomiser {
    double nextDouble(double bound);
}
