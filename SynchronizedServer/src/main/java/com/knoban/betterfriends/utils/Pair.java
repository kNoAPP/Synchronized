package com.knoban.betterfriends.utils;

/**
 * Written for EAE-3720 at the University of Utah.
 * @author Alden Bansemer (kNoAPP)
 *
 * This is meant to replace JavaFX which is being annoying buggy in Linux distros as of late.
 */
public class Pair<T, U> {

    private T t;
    private U u;

    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getKey() {
        return t;
    }

    public U getValue() {
        return u;
    }
}

