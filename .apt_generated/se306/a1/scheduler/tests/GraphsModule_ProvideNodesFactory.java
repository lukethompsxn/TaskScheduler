package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Map;
import javax.annotation.Generated;
import se306.a1.scheduler.data.Node;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GraphsModule_ProvideNodesFactory implements Factory<Map<String, Node>> {
  private final GraphsModule module;

  public GraphsModule_ProvideNodesFactory(GraphsModule module) {
    this.module = module;
  }

  @Override
  public Map<String, Node> get() {
    return Preconditions.checkNotNull(
        module.provideNodes(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static GraphsModule_ProvideNodesFactory create(GraphsModule module) {
    return new GraphsModule_ProvideNodesFactory(module);
  }

  public static Map<String, Node> proxyProvideNodes(GraphsModule instance) {
    return Preconditions.checkNotNull(
        instance.provideNodes(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
