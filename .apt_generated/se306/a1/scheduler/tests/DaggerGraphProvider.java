package se306.a1.scheduler.tests;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import se306.a1.scheduler.data.Graph;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerGraphProvider implements GraphProvider {
  private GraphsModule graphsModule;

  private DaggerGraphProvider(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static GraphProvider create() {
    return new Builder().build();
  }

  private TestGraph getTestGraph() {
    return injectTestGraph(TestGraph_Factory.newTestGraph());
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.graphsModule = builder.graphsModule;
  }

  @Override
  public Graph injectGraph() {
    return GraphsModule_ProvideGraphFactory.proxyProvideGraph(graphsModule, getTestGraph());
  }

  @CanIgnoreReturnValue
  private TestGraph injectTestGraph(TestGraph instance) {
    TestGraph_MembersInjector.injectChildren(
        instance, GraphsModule_ProvideChildrenFactory.proxyProvideChildren(graphsModule));
    TestGraph_MembersInjector.injectParents(
        instance, GraphsModule_ProvideParentsFactory.proxyProvideParents(graphsModule));
    TestGraph_MembersInjector.injectNodes(
        instance, GraphsModule_ProvideNodesFactory.proxyProvideNodes(graphsModule));
    TestGraph_MembersInjector.injectName(
        instance, GraphsModule_ProvideNameFactory.proxyProvideName(graphsModule));
    return instance;
  }

  public static final class Builder {
    private GraphsModule graphsModule;

    private Builder() {}

    public GraphProvider build() {
      if (graphsModule == null) {
        this.graphsModule = new GraphsModule();
      }
      return new DaggerGraphProvider(this);
    }

    public Builder graphsModule(GraphsModule graphsModule) {
      this.graphsModule = Preconditions.checkNotNull(graphsModule);
      return this;
    }
  }
}
