package se306.a1.scheduler.tests;

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
public final class DaggerGraphComponent implements GraphComponent {
  private GraphsModule graphsModule;

  private DaggerGraphComponent(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static GraphComponent create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.graphsModule = builder.graphsModule;
  }

  @Override
  public Map<Node, List<Edge>> injectChildren() {
    return GraphsModule_ProvideChildrenFactory.proxyProvideChildren(graphsModule);
  }

  @Override
  public Map<Node, List<Node>> injectParents() {
    return GraphsModule_ProvideParentsFactory.proxyProvideParents(graphsModule);
  }

  @Override
  public Map<String, Node> injectNodes() {
    return GraphsModule_ProvideNodesFactory.proxyProvideNodes(graphsModule);
  }

  @Override
  public String injectName() {
    return GraphsModule_ProvideNameFactory.proxyProvideName(graphsModule);
  }

  public static final class Builder {
    private GraphsModule graphsModule;

    private Builder() {}

    public GraphComponent build() {
      if (graphsModule == null) {
        this.graphsModule = new GraphsModule();
      }
      return new DaggerGraphComponent(this);
    }

    public Builder graphsModule(GraphsModule graphsModule) {
      this.graphsModule = Preconditions.checkNotNull(graphsModule);
      return this;
    }
  }
}
