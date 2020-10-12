import { EventEmitter } from "events";
import { decorate, injectable } from "inversify";
import { IoCContainer } from "./bindings/inversify.config";
import { TYPES } from "./bindings/types";
import { SocketManager } from "./server/Socket";

decorate(injectable(), EventEmitter);
const socketManager = IoCContainer.get<SocketManager>(TYPES.SocketManager);
