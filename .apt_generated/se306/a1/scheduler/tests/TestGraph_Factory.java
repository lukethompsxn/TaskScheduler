package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.inject.Provider;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Node;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class TestGraph_Factory implements Factory<TestGraph> {
  private final Provider<Map<Node, List<Edge>>> childrenProvider;

  private final Provider<Map<Node, List<Node>>> parentsProvider;

  private final Provider<Map<String, Node>> nodesProvider;

  private final Provider<String> nameProvider;

  public TestGraph_Factory(
      Provider<Map<Node, List<Edge>>> childrenProvider,
      Provider<Map<Node, List<Node>>> parentsProvider,
      Provider<Map<String, Node>> nodesProvider,
      Provider<String> nameProvider) {
    this.childrenProvider = childrenProvider;
    this.parentsProvider = parentsProvider;
    this.nodesProvider = nodesProvider;
    this.nameProvider = nameProvider;
  }

  @Override
  public TestGraph get() {
    TestGraph instance = new TestGraph();
    TestGraph_MembersInjector.injectChildren(instance, childrenProvider.get());
    TestGraph_MembersInjector.injectParents(instance, parentsProvider.get());
    TestGraph_MembersInjector.injectNodes(instance, nodesProvider.get());
    TestGraph_MembersInjector.injectName(instance, nameProvider.get());
    return instance;
  }

  public static TestGraph_Factory create(
      Provider<Map<Node, List<Edge>>> childrenProvider,
      Provider<Map<Node, List<Node>>> parentsProvider,
      Provider<Map<String, Node>> nodesProvider,
      Provider<String> nameProvider) {
    return new TestGraph_Factory(childrenProvider, parentsProvider, nodesProvider, nameProvider);
  }

  public static TestGraph newTestGraph() {
    return new TestGraph();
  }
}
