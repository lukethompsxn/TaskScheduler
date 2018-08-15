package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import se306.a1.scheduler.data.Graph;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GraphsModule_ProvideGraphFactory implements Factory<Graph> {
  private final GraphsModule module;

  private final Provider<TestGraph> gProvider;

  public GraphsModule_ProvideGraphFactory(GraphsModule module, Provider<TestGraph> gProvider) {
    this.module = module;
    this.gProvider = gProvider;
  }

  @Override
  public Graph get() {
    return Preconditions.checkNotNull(
        module.provideGraph(gProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static GraphsModule_ProvideGraphFactory create(
      GraphsModule module, Provider<TestGraph> gProvider) {
    return new GraphsModule_ProvideGraphFactory(module, gProvider);
  }

  public static Graph proxyProvideGraph(GraphsModule instance, TestGraph g) {
    return Preconditions.checkNotNull(
        instance.provideGraph(g), "Cannot return null from a non-@Nullable @Provides method");
  }
}
