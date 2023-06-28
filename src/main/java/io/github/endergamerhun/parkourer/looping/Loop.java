package io.github.endergamerhun.parkourer.looping;

import io.github.endergamerhun.parkourer.utils.TaskUtil;

public abstract class Loop implements Runnable {
    protected boolean first = true;

    protected abstract boolean loop();
    protected abstract void end();

    public void run() {
        if (loop()) {
            first = false;
            TaskUtil.runLater(this);
        } else if (!first) {
            end();
        }
    }
}
