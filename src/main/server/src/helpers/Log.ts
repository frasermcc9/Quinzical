import "reflect-metadata";
import {inject, injectable} from "inversify";
import chalk = require("chalk");
import figlet = require("figlet");

@injectable()
export default class LogImpl implements Log {
    constructor(
        @inject("console") private readonly console: Console,
        @inject("process") private readonly process: NodeJS.Process
    ) {}

    debug(src: string, msg: string): void {
        this.log("DEBUG", src, msg);
    }

    trace(src: string, msg: string, error?: Error): void {
        this.log("TRACE", src, msg, error);
    }

    info(src: string, msg: string, error?: Error): void {
        this.log("INFO", src, msg, error);
    }

    warn(src: string, msg: string, error?: Error): void {
        this.log("WARN", src, msg, error);
    }

    error(src: string, msg: string, error?: Error): void {
        this.log("ERROR", src, msg, error);
    }

    critical(src: string, msg: string, error?: Error): void {
        this.log("CRITICAL", src, msg, error);
        this.process.exit();
    }

    logo(name: string): void {
        this.console.log(chalk.blue(figlet.textSync(name)));
        this.console.log();
    }

    private log(
        severity: Severity,
        src: string,
        msg: string,
        error?: Error
    ): void {
        const c = LogImpl.colorResolver(severity);
        this.console.log(
            c.bold(`[${severity}] `) +
                c(`${src} - ${msg}${LogImpl.formatError(error)}`)
        );
    }

    private static colorResolver(severity: Severity) {
        switch (severity) {
            case "DEBUG":
                return chalk.grey;
            case "TRACE":
                return chalk.whiteBright;
            case "INFO":
                return chalk.green;
            case "WARN":
                return chalk.yellow;
            case "ERROR":
                return chalk.red;
            default:
                return chalk.whiteBright.bgRed;
        }
    }

    private static formatError(error?: Error): string {
        if (error != null) {
            const stack = error.stack!.replace(
                error.name + ": " + error.message + "\n",
                ""
            );
            return (
                "\r\n" +
                chalk.bold(error.name) +
                ": " +
                error.message +
                "\r\n" +
                chalk.gray(stack)
            );
        } else {
            return "";
        }
    }
}

export interface Log {
    debug(src: string, msg: string): void;

    trace(src: string, msg: string, error?: Error): void;

    info(src: string, msg: string, error?: Error): void;

    warn(src: string, msg: string, error?: Error): void;

    error(src: string, msg: string, error?: Error): void;

    critical(src: string, msg: string, error?: Error): void;

    logo(name: string): void;
}

type Severity = "DEBUG" | "TRACE" | "INFO" | "WARN" | "ERROR" | "CRITICAL";
