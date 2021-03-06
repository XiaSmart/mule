/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.file.internal.command;

import static java.lang.String.format;
import org.mule.extension.file.api.FileConnector;
import org.mule.extension.file.api.LocalFileSystem;
import org.mule.runtime.module.extension.file.api.command.FileCommand;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base class for implementations of {@link FileCommand} which operate
 * on a local file system
 *
 * @since 4.0
 */
abstract class LocalFileCommand extends FileCommand<FileConnector, LocalFileSystem>
{

    /**
     * {@inheritDoc}
     */
    LocalFileCommand(LocalFileSystem fileSystem, FileConnector config)
    {
        super(fileSystem, config);
    }

    /**
     * @return a {@link Path} derived from {@link FileConnector#getBaseDir()}
     */
    @Override
    protected Path getBasePath()
    {
        return Paths.get(config.getBaseDir());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean exists(Path path)
    {
        return Files.exists(path);
    }

    /**
     * Transforms the {@code directoryPath} to a {@link} {@link File}
     * and invokes {@link File#mkdirs()} on it
     *
     * @param directoryPath a {@link Path} pointing to the directory you want to create
     */
    @Override
    protected void doMkDirs(Path directoryPath)
    {
        File target = directoryPath.toFile();
        try
        {
            if (!target.mkdirs())
            {
                throw exception(format("Directory '%s' could not be created", target));
            }
        }
        catch (Exception e)
        {
            throw exception(format("Exception was found creating directory '%s'", target), e);
        }
    }
}
