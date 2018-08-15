package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import se306.a1.scheduler.data.Node;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GraphsModule_ProvideParentsFactory implements Factory<Map<Node, List<Node>>> {
  private final GraphsModule module;

  public GraphsModule_ProvideParentsFactory(GraphsModule module) {
    this.module = module;
  }

  @Override
  public Map<Node, List<Node>> get() {
    return Preconditions.checkNotNull(
        module.provideParents(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static GraphsModule_ProvideParentsFactory create(GraphsModule module) {
    return new GraphsModule_ProvideParentsFactory(module);
  }

  public static Map<Node, List<Node>> proxyProvideParents(GraphsModule instance) {
    return Preconditions.checkNotNull(
        instance.provideParents(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
