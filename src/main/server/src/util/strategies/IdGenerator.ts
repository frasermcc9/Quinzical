import {injectable} from "inversify";

@injectable()
class IdGeneratorContextImpl implements IdGenerationContext {
    generateIdStrategy(type: "FIXED" | "RANDOM"): IdGenerationStrategy {
        switch (type) {
            case "FIXED":
                return new FixedIdGenerator();

            case "RANDOM":
                return new RandomIdGenerator();

            default:
                throw new TypeError(`${type} is not a valid ID Generation Strategy.`);
        }
    }
}

class RandomIdGenerator implements IdGenerationStrategy {
    static alphabet = "abcdefghijklmnopqrstuvwxyz";

    generateId(): string {
        let id = "";
        for (let i = 0; i < 4; i++) {
            const randomCharacter = RandomIdGenerator.alphabet[~~(Math.random() * RandomIdGenerator.alphabet.length)];
            id += randomCharacter;
        }
        return id.toUpperCase();
    }
}

/**
 * Generates a fixed id of abcd
 */
class FixedIdGenerator implements IdGenerationStrategy {
    generateId(): string {
        return "ABCD";
    }
}

interface IdGenerationStrategy {
    generateId(): string;
}

interface IdGenerationContext {
    generateIdStrategy(type: "FIXED" | "RANDOM"): IdGenerationStrategy;
}

export { IdGenerationContext, IdGeneratorContextImpl };
