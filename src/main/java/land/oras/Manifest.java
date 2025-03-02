/*-
 * =LICENSE=
 * ORAS Java SDK
 * ===
 * Copyright (C) 2024 - 2025 ORAS
 * ===
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =LICENSEEND=
 */

package land.oras;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import land.oras.utils.Const;
import land.oras.utils.JsonUtils;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

/**
 * Class for manifest
 */
@NullUnmarked
public final class Manifest {

    private final int schemaVersion;
    private final String mediaType;
    private final String artifactType;
    private final Config config;
    private final Subject subject;
    private final List<Layer> layers;
    private final Map<String, String> annotations;

    /**
     * The manifest descriptor
     */
    private final transient ManifestDescriptor descriptor;

    private Manifest(
            int schemaVersion,
            String mediaType,
            ArtifactType artifactType,
            ManifestDescriptor descriptor,
            Config config,
            Subject subject,
            List<Layer> layers,
            Annotations annotations) {
        this.schemaVersion = schemaVersion;
        this.mediaType = mediaType;
        this.artifactType = artifactType != null ? artifactType.getMediaType() : null;
        this.descriptor = descriptor;
        this.config = config;
        this.subject = subject;
        this.layers = layers;
        this.annotations = Map.copyOf(annotations.manifestAnnotations());
    }

    /**
     * Get the schema version
     * @return The schema version
     */
    public int getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Get the media type
     * @return The media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Get the artifact type
     * @return The artifact type
     */
    public @NonNull ArtifactType getArtifactType() {
        if (artifactType != null) {
            return ArtifactType.from(artifactType);
        }
        if (config != null) {
            return ArtifactType.from(
                    config.getMediaType() != null ? config.getMediaType() : Const.DEFAULT_ARTIFACT_MEDIA_TYPE);
        }
        return ArtifactType.unknown();
    }

    /**
     * Get the descriptor
     * @return The descriptor
     */
    public ManifestDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Get the config
     * @return The config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Get the subject
     * @return The subject
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Get the layers
     * @return The layers
     */
    public List<Layer> getLayers() {
        return layers != null ? Collections.unmodifiableList(layers) : List.of();
    }

    /**
     * Get the annotations
     * @return The annotations
     */
    public Map<String, String> getAnnotations() {
        if (annotations == null) {
            return Map.of();
        }
        return annotations;
    }

    /**
     * Return a new manifest with the given artifact type
     * @param artifactType The artifact type
     * @return The manifest
     */
    public Manifest withArtifactType(ArtifactType artifactType) {
        return new Manifest(
                schemaVersion,
                mediaType,
                artifactType,
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return a new manifest with the given layers
     * @param layers The layers
     * @return The manifest
     */
    public Manifest withLayers(List<Layer> layers) {
        return new Manifest(
                schemaVersion,
                mediaType,
                getTopLevelArtifactType(),
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return a new manifest with the given config
     * @param config The config
     * @return The manifest
     */
    public Manifest withConfig(Config config) {
        return new Manifest(
                schemaVersion,
                mediaType,
                getTopLevelArtifactType(),
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return a new manifest with the given config
     * @param subject The subject
     * @return The manifest
     */
    public Manifest withSubject(Subject subject) {
        return new Manifest(
                schemaVersion,
                mediaType,
                getTopLevelArtifactType(),
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return a new manifest with the given annotations
     * @param annotations The annotations
     * @return The manifest
     */
    public Manifest withAnnotations(Map<String, String> annotations) {
        return new Manifest(
                schemaVersion,
                mediaType,
                getTopLevelArtifactType(),
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return a new manifest with the given descriptor
     * @param descriptor The descriptor
     * @return The manifest
     */
    public Manifest withDescriptor(ManifestDescriptor descriptor) {
        return new Manifest(
                schemaVersion,
                mediaType,
                getTopLevelArtifactType(),
                descriptor,
                config,
                subject,
                layers,
                Annotations.ofManifest(annotations));
    }

    /**
     * Return the JSON representation of the manifest
     * @return The JSON string
     */
    public String toJson() {
        return JsonUtils.toJson(this);
    }

    /**
     * Create a manifest from a JSON string
     * @param json The JSON string
     * @return The manifest
     */
    public static Manifest fromJson(String json) {
        return JsonUtils.fromJson(json, Manifest.class);
    }

    private @Nullable ArtifactType getTopLevelArtifactType() {
        if (artifactType != null) {
            return ArtifactType.from(artifactType);
        }
        return null;
    }

    /**
     * Return a copy of an empty manifest
     * @return The empty manifest
     */
    public static Manifest empty() {
        ManifestDescriptor descriptor = ManifestDescriptor.of(
                Const.DEFAULT_EMPTY_MEDIA_TYPE,
                "sha256:44136fa355b3678a1146ad16f7e8649e94fb4fc21fe77e8310c060f61caaff8a",
                2);
        return new Manifest(
                2,
                Const.DEFAULT_MANIFEST_MEDIA_TYPE,
                null,
                descriptor,
                Config.empty(),
                null,
                List.of(),
                Annotations.empty());
    }
}
