package ar.edu.itba.genetic_algorithms.main.io;

import ar.edu.itba.genetic_algorithms.models.alleles.ItemsRepository;
import ar.edu.itba.genetic_algorithms.models.item.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is in charge of parsing item's files, and storing them to an {@link ItemsRepository}.
 */
public class ItemsFileReader {


    /**
     * The repository in which the {@link Item}s will be stored.
     */
    private final ItemsRepository repository;

    /**
     * Array containing headers as they were read in the file.
     */
    private final Header[] headers;

    /**
     * The kind of {@link Item} being read.
     */
    private final ItemType itemType;

    /**
     * A {@link List} of {@link String} containing each line read in the file.
     */
    private final List<String> data;


    /**
     * Constructor.
     *
     * @param path       The path to the item file.
     * @param repository The repository in which the {@link Item}s will be stored.
     * @param itemType   The kind of {@link Item} being read.
     * @throws IOException If an IO error occurs while reading the file with the given {@code path}.
     */
    public ItemsFileReader(String path, ItemsRepository repository, ItemType itemType) throws IOException {
        this.data = Files.lines(Paths.get(path)).collect(Collectors.toList());
        this.repository = repository;
        this.itemType = itemType;
        this.headers = new Header[Header.values().length];
    }

    /**
     * Parses the file.
     */
    public void parse() {
        // Parse headers
        Arrays.stream(data.remove(0).split("\t")).map(Header::fromStringInFile)
                .collect(Collectors.toList())
                .toArray(headers);
        data.stream().parallel().collect(new ItemsCollector(repository, headers, itemType));
    }



    /**
     * Enum indicating the type of {@link Item} supported by the system.
     */
    public enum ItemType {
        ARMOR {
            @Override
            public Item.Builder<? extends Item> getBuilder() {
                return new Armor.Builder();
            }
        },
        BOOT {
            @Override
            public Item.Builder<? extends Item> getBuilder() {
                return new Boot.Builder();
            }
        },
        GAUNTLET {
            @Override
            public Item.Builder<? extends Item> getBuilder() {
                return new Gauntlet.Builder();
            }
        },
        HELMET {
            @Override
            public Item.Builder<? extends Item> getBuilder() {
                return new Helmet.Builder();
            }
        },
        WEAPON {
            @Override
            public Item.Builder<? extends Item> getBuilder() {
                return new Weapon.Builder();
            }
        };

        /**
         * Returns an {@link Item.Builder} according to the enum value.
         *
         * @return The builder.
         */
        public abstract Item.Builder<? extends Item> getBuilder();
    }


    /**
     * Class wrapping data for creating and storing an {@link Item} in the {@link ItemsRepository}.
     */
    private static class DataWrapper {

        /**
         * Id in the repository.
         */
        private int id;

        /**
         * The item's strength.
         */
        private double strength;

        /**
         * The item's agility.
         */
        private double agility;

        /**
         * The item's proficiency.
         */
        private double proficiency;

        /**
         * The item's resistance.
         */
        private double resistance;

        /**
         * The item's life.
         */
        private double life;


        /**
         * Id getter.
         *
         * @return The id in the repository.
         */
        private int getId() {
            return id;
        }

        /**
         * Strength getter.
         *
         * @return The item's strength.
         */
        private double getStrength() {
            return strength;
        }

        /**
         * Agility getter.
         *
         * @return The item's agility.
         */
        private double getAgility() {
            return agility;
        }

        /**
         * Proficiency getter.
         *
         * @return The item's proficiency.
         */
        private double getProficiency() {
            return proficiency;
        }

        /**
         * Resistance getter.
         *
         * @return The item's resistance.
         */
        private double getResistance() {
            return resistance;
        }

        /**
         * Life getter.
         *
         * @return The item's life.
         */
        private double getLife() {
            return life;
        }

        /**
         * Id setter.
         *
         * @param id The id in the repository.
         */
        private void setId(int id) {
            this.id = id;
        }

        /**
         * Strength setter.
         *
         * @param strength The item's strength.
         */
        private void setStrength(double strength) {
            this.strength = strength;
        }

        /**
         * Strength agility.
         *
         * @param agility The item's agility.
         */
        private void setAgility(double agility) {
            this.agility = agility;
        }

        /**
         * Proficiency setter.
         *
         * @param proficiency The item's proficiency.
         */
        private void setProficiency(double proficiency) {
            this.proficiency = proficiency;
        }

        /**
         * Resistance setter.
         *
         * @param resistance The item's resistance.
         */
        private void setResistance(double resistance) {
            this.resistance = resistance;
        }

        /**
         * Life setter.
         *
         * @param life The item's lifes.
         */
        private void setLife(double life) {
            this.life = life;
        }
    }


    private enum Header {
        ID {
            /**
             * Takes the id from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Integer}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setId(Integer.valueOf(data));
            }
        },
        STRENGTH {
            /**
             * Takes the strength from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Double}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setStrength(Double.valueOf(data));
            }
        },
        AGILITY {
            /**
             * Takes the agility from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Double}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setAgility(Double.valueOf(data));
            }
        },
        PROFICIENCY {
            /**
             * Takes the proficiency from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Double}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setProficiency(Double.valueOf(data));
            }
        },
        RESISTANCE {
            /**
             * Takes the resistance from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Double}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setResistance(Double.valueOf(data));
            }
        },
        LIFE {
            /**
             * Takes the life from the given {@code data}.
             *
             * @param data    {@link String} from where data must be taken.
             * @param wrapper The {@link DataWrapper} where data is being wrapped.
             * @implNote The given {@link String} must represent an {@link Double}.
             */
            @Override
            protected void wrapData(String data, DataWrapper wrapper) {
                wrapper.setLife(Double.valueOf(data));
            }
        };

        /**
         * Takes data from the given {@link String}, and wraps it into the given {@code wrapper}.
         *
         * @param data    {@link String} from where data must be taken.
         * @param wrapper The {@link DataWrapper} where data is being wrapped.
         */
        protected abstract void wrapData(String data, DataWrapper wrapper);


        /**
         * Returns an enum value based on the given {@code str}.
         *
         * @param str The {@link String} from which the enum value must be returned.
         * @return The enum value.
         */
        private static Header fromStringInFile(String str) {
            switch (str.toLowerCase()) {
                case "id":
                    return ID;
                case "fu":
                    return STRENGTH;
                case "ag":
                    return AGILITY;
                case "ex":
                    return PROFICIENCY;
                case "re":
                    return RESISTANCE;
                case "vi":
                    return LIFE;
                default:
                    throw new IllegalArgumentException("Unrecognized header."); // TODO: create exception for this.
            }
        }
    }

    /**
     * {@link Collector} to transform a {@link java.util.stream.Stream} of {@link String}
     * into an {@link ItemsRepository}.
     */
    private static final class ItemsCollector implements Collector<String, Map<Integer, Item>, ItemsRepository> {

        /**
         * The repository in which the {@link Item}s will be stored.
         */
        private final ItemsRepository repository;

        /**
         * Array containing headers as they were read in the file.
         */
        private final Header[] headers;

        /**
         * The kind of {@link Item} being read.
         */
        private final ItemType itemType;

        /**
         * Constructor.
         *
         * @param repository The repository in which the {@link Item}s will be stored.
         * @param headers    Array containing headers as they were read in the file.
         * @param itemType   The kind of {@link Item} being read.
         */
        private ItemsCollector(ItemsRepository repository, Header[] headers, ItemType itemType) {
            this.repository = repository;
            this.headers = headers;
            this.itemType = itemType;
        }

        @Override
        public Supplier<Map<Integer, Item>> supplier() {
            return HashMap::new;
        }

        @Override
        public BiConsumer<Map<Integer, Item>, String> accumulator() {
            return (map, str) -> {
                final String[] line = str.split("\t");
                // Check file is correct.
                if (line.length != headers.length) {
                    throw new IllegalArgumentException("Wrong file"); // TODO: create exception for this.
                }

                final DataWrapper wrapper = new DataWrapper();
                IntStream.range(0, headers.length).forEach(idx -> headers[idx].wrapData(line[idx], wrapper));
                map.put(wrapper.getId(), itemType.getBuilder()
                        .setStrength(wrapper.getStrength())
                        .setAgility(wrapper.getAgility())
                        .setProficiency(wrapper.getProficiency())
                        .setResistance(wrapper.getResistance())
                        .setLife(wrapper.getLife())
                        .build());
            };
        }

        @Override
        public BinaryOperator<Map<Integer, Item>> combiner() {
            return (map1, map2) -> {
                map1.putAll(map2);
                return map1;
            };
        }

        @Override
        public Function<Map<Integer, Item>, ItemsRepository> finisher() {
            return (map) -> {
                map.entrySet().forEach(entry -> repository.addItem(entry.getKey(), entry.getValue()));
                return repository;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.singleton(Characteristics.CONCURRENT);
        }
    }


}
