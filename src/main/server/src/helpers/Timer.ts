import {injectable} from "inversify";

@injectable()
class TimerImpl implements Timer {
    private id?: NodeJS.Timeout;
    private started?: Date;
    private running: boolean = false;

    private callback?: () => void;
    private delay?: number;

    private remaining?: number;

    start(): void {
        this.running = true;
        this.started = new Date();

        if (this.callback === undefined || this.remaining === undefined) {
            throw new ReferenceError("Delay and Function was not set for Timer.");
        }

        this.id = setTimeout(this.callback, this.remaining);
    }

    stop(): void {
        this.running = false;
        clearTimeout(this.id!);
        this.remaining = this.delay;
    }

    getTimeLeft(): number {
        return this.delay! - (new Date().getTime() - this.started!.getTime());
    }

    getStateRunning(): boolean {
        return this.running;
    }

    setFunction(callback: () => void): this {
        this.callback = callback;
        return this;
    }

    setDelay(delay: number): this {
        this.delay = delay;
        this.remaining = delay;
        return this;
    }
}

interface Timer {
    start(): void;
    stop(): void;
    getTimeLeft(): number;
    getStateRunning(): boolean;
    setFunction(callback: () => void): this;
    setDelay(delay: number): this;
}

export { Timer, TimerImpl };
