package se306.a1.scheduler.tests;

import dagger.MembersInjector;
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
public final class TestGraph_MembersInjector implements MembersInjector<TestGraph> {
  private final Provider<Map<Node, List<Edge>>> childrenProvider;

  private final Provider<Map<Node, List<Node>>> parentsProvider;

  private final Provider<Map<String, Node>> nodesProvider;

  private final Provider<String> nameProvider;

  public TestGraph_MembersInjector(
      Provider<Map<Node, List<Edge>>> childrenProvider,
      Provider<Map<Node, List<Node>>> parentsProvider,
      Provider<Map<String, Node>> nodesProvider,
      Provider<String> nameProvider) {
    this.childrenProvider = childrenProvider;
    this.parentsProvider = parentsProvider;
    this.nodesProvider = nodesProvider;
    this.nameProvider = nameProvider;
  }

  public static MembersInjector<TestGraph> create(
      Provider<Map<Node, List<Edge>>> childrenProvider,
      Provider<Map<Node, List<Node>>> parentsProvider,
      Provider<Map<String, Node>> nodesProvider,
      Provider<String> nameProvider) {
    return new TestGraph_MembersInjector(
        childrenProvider, parentsProvider, nodesProvider, nameProvider);
  }

  @Override
  public void injectMembers(TestGraph instance) {
    injectChildren(instance, childrenProvider.get());
    injectParents(instance, parentsProvider.get());
    injectNodes(instance, nodesProvider.get());
    injectName(instance, nameProvider.get());
  }

  public static void injectChildren(TestGraph instance, Map<Node, List<Edge>> children) {
    instance.children = children;
  }

  public static void injectParents(TestGraph instance, Map<Node, List<Node>> parents) {
    instance.parents = parents;
  }

  public static void injectNodes(TestGraph instance, Map<String, Node> nodes) {
    instance.nodes = nodes;
  }

  public static void injectName(TestGraph instance, String name) {
    instance.name = name;
  }
}
