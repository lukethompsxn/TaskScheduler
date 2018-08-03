package se306.a1.scheduler.tests;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GraphsModule_ProvideNameFactory implements Factory<String> {
  private final GraphsModule module;

  public GraphsModule_ProvideNameFactory(GraphsModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.provideName(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static GraphsModule_ProvideNameFactory create(GraphsModule module) {
    return new GraphsModule_ProvideNameFactory(module);
  }

  public static String proxyProvideName(GraphsModule instance) {
    return Preconditions.checkNotNull(
        instance.provideName(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
