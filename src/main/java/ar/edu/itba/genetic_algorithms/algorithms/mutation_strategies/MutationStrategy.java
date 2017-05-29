package ar.edu.itba.genetic_algorithms.algorithms.mutation_strategies;

import ar.edu.itba.genetic_algorithms.algorithms.api.AlleleContainerWrapper;
import ar.edu.itba.genetic_algorithms.algorithms.api.Chromosome;

public interface MutationStrategy {

    /**
     * Mutates a chromosome.
     *
     * @param originalChromosome the original chromosome for mutation.
     * @param alleleContainerWrapper container of the possible alleles.
     */
    void mutate(Chromosome originalChromosome, AlleleContainerWrapper alleleContainerWrapper);

}
