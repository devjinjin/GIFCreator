package com.devjinjin.gifcreator.GifManager;


import java.util.Iterator;

/**
 * ISL_KOREA
 * Created by jylee on 2018-01-04.
 */

public abstract class GifImageIterator implements Iterator<GifImage> {

    @Override
    public abstract boolean hasNext();

    @Override
    public abstract GifImage next();

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    // It must be called last to return memory resources.
    public abstract void close();
}