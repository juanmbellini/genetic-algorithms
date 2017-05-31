package ar.edu.itba.genetic_algorithms.algorithms.replacement_strategies;

import ar.edu.itba.genetic_algorithms.algorithms.api.Chromosome;
import ar.edu.itba.genetic_algorithms.algorithms.api.Individual;
import ar.edu.itba.genetic_algorithms.algorithms.api.IndividualCreator;
import ar.edu.itba.genetic_algorithms.algorithms.engine.Population;
import ar.edu.itba.genetic_algorithms.algorithms.selection_strategies.SelectionStrategy;
import ar.edu.itba.genetic_algorithms.models.character.Archer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the second replacement strategy, which consists of creating a new {@link Population}
 * replacing only a certain amount of {@link Individual}s
 * (those not replaced continue to exist in the new {@link Population}).
 */
public class ReplacementStrategy2 implements ReplacementStrategy {

    private final SelectionStrategy oldPopulationSelectionStrategy;

    private final IndividualCreator individualCreator;

    public ReplacementStrategy2(SelectionStrategy oldPopulationSelectionStrategy, IndividualCreator individualCreator){
        this.oldPopulationSelectionStrategy = oldPopulationSelectionStrategy;
        this.individualCreator = individualCreator;
    }

    @Override
    public Population replace(Population actualPopulation, List<Individual> offspring) {
        if (offspring.size() > actualPopulation.getPopulationSize()) {
            throw new IllegalArgumentException("This replacement strategy requires " +
                    "a list of offspring whose size is smaller or equal than the size of the population");
        }

        int k = offspring.size();
        List<Individual> newIndividuals = oldPopulationSelectionStrategy.select(actualPopulation, actualPopulation.getPopulationSize() - k).
                stream().map(individualCreator::create).collect(Collectors.toList());
        newIndividuals.addAll(offspring);
        return new Population(newIndividuals, actualPopulation);
    }

}
