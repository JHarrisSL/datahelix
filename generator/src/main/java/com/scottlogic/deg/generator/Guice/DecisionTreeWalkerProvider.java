package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

public class DecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {
    private final DecisionTreeWalker reductiveDecisionTreeWalker;
    private final DecisionTreeWalker cartesianProductDecisionTreeWalker;
    private final DecisionTreeWalker routedDecisionTreeWalker;
    private final GenerationConfigSource commandLine;

    @Inject
    public DecisionTreeWalkerProvider(
        @Named("reductive") DecisionTreeWalker reductiveDecisionTreeWalker,
        @Named("cartesian") DecisionTreeWalker cartesianProductDecisionTreeWalker,
        @Named("routed") DecisionTreeWalker routedDecisionTreeWalker,
        GenerationConfigSource commandLine) {
        this.reductiveDecisionTreeWalker = reductiveDecisionTreeWalker;
        this.cartesianProductDecisionTreeWalker = cartesianProductDecisionTreeWalker;
        this.routedDecisionTreeWalker = routedDecisionTreeWalker;
        this.commandLine = commandLine;
    }

    @Override
            public DecisionTreeWalker get() {
          switch(this.commandLine.getWalkerType()) {
              case CARTESIAN_PRODUCT:
                return this.cartesianProductDecisionTreeWalker;

              case REDUCTIVE:
                return this.reductiveDecisionTreeWalker;

              case ROUTED:
                return this.routedDecisionTreeWalker;
        }
        return null;
    }
}
