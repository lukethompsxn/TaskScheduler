package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Node;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GraphsModule_ProvideChildrenFactory implements Factory<Map<Node, List<Edge>>> {
  private final GraphsModule module;

  public GraphsModule_ProvideChildrenFactory(GraphsModule module) {
    this.module = module;
  }

  @Override
  public Map<Node, List<Edge>> get() {
    return Preconditions.checkNotNull(
        module.provideChildren(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static GraphsModule_ProvideChildrenFactory create(GraphsModule module) {
    return new GraphsModule_ProvideChildrenFactory(module);
  }

  public static Map<Node, List<Edge>> proxyProvideChildren(GraphsModule instance) {
    return Preconditions.checkNotNull(
        instance.provideChildren(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
