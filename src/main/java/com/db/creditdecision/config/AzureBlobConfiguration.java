package com.db.creditdecision.config;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.beans.factory.annotation.Value;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudAppendBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

/**
 * Appends log events to Azure Storage Blob.
 * 
 */
@Plugin(name = "AzureBlobAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class AzureBlobConfiguration extends AbstractAppender {

	@Value("${app.db.api.logger.prefix}")
	private static String loggerPrefix= "ControllerCheck";

	private static String SASURL = "https://creditdecisionstorage.blob.core.windows.net/testingblob?sp=racwdl&st=2021-06-29T04:47:18Z&se=2021-06-30T12:47:18Z&sv=2020-02-10&sr=c&sig=GgSYGwymS%2Bm9thPtLm6UdzxfUHNDofRNhKtZOO7tCio%3D";

	private CloudBlobContainer _container;
	private String _prefix1;
	private String _prefix2="InsideLogger";

	protected AzureBlobConfiguration(String name, Filter filter, Layout<? extends Serializable> layout,
			final boolean ignoreExceptions, final Property[] properties, String sas, String websiteName,
			String websiteId) throws StorageException {
		super(name, filter, layout, ignoreExceptions, properties);
		_container = new CloudBlobContainer(URI.create(sas));
		_prefix1 = websiteName;
		_prefix2 = websiteId;
	}

	protected AzureBlobConfiguration(String name, Filter filter, Layout<? extends Serializable> layout,
			final boolean ignoreExceptions, final Property[] properties, String accountName, String accountKey,
			String containerName, String prefix1, String prefix2) throws StorageException, URISyntaxException {

		super(name, filter, layout, ignoreExceptions, properties);
		StorageCredentialsAccountAndKey creds = new StorageCredentialsAccountAndKey(accountName, accountKey);
		_container = (new CloudStorageAccount(creds, true)).createCloudBlobClient()
				.getContainerReference(containerName);
		_container.createIfNotExists();
		_prefix1 = prefix1;
		_prefix2 = prefix2;
	}

	@Override
	public void append(LogEvent event) {

		String name = getBlobname();

		try {
			CloudAppendBlob blob = _container.getAppendBlobReference(name);
			if (!blob.exists()) {
				blob.createOrReplace();
			}
			byte[] bytes = getLayout().toByteArray(event);

			blob.appendFromByteArray(bytes, 0, bytes.length);
		} catch (URISyntaxException | StorageException | IOException e) {

			if (!ignoreExceptions()) {
				throw new AppenderLoggingException(e);
			}
		}
	}

	private String getBlobname() {
		String dfmt = (new SimpleDateFormat("yyyy/MM/dd/HH")).format(new Date());
		if (isNullOrEmpty(_prefix2)) {
			return String.format("%s/%s_applicationLog.txt", dfmt, _prefix1);
		} else {
			return String.format("%s/%s/%s_applicationLog.txt", _prefix1, dfmt, _prefix2);
		}
	}

	/**
	 * Create AzureBlobAppender.
	 * 
	 * @param name          The name of the Appender.
	 * @param webapps       WebApps mode. If this value is true, assume it is
	 *                      running on WebApps.
	 * @param accountName   Azure storage account name. It becomes effective when
	 *                      WebApps is false.
	 * @param accountKey    Azure storage account key. It becomes effective when
	 *                      WebApps is false.
	 * @param containerName The name of blob container. It becomes effective when
	 *                      WebApps is false.
	 * @param prefix1       Specify directory structure. It becomes effective when
	 *                      WebApps is false.
	 * @param prefix2       Specify directory structure. It becomes effective when
	 *                      WebApps is false. Can be null, empty or unset.
	 * @param layout        The layout to format the message.
	 * @param filter        The filter to filter the message.
	 * @return AzureBlobAppender instance.
	 */
	@PluginFactory
	public static AzureBlobConfiguration createAppender(@PluginAttribute("name") String name,
			@PluginAttribute("webapps") boolean webapps, @PluginAttribute("accountName") final String accountName,
			@PluginAttribute("accountKey") final String accountKey,
			@PluginAttribute("containerName") final String containerName, @PluginAttribute("prefix1") String prefix1,
			@PluginAttribute("prefix2") String prefix2, @PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") final Filter filter) {

		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}

		try {
			return new AzureBlobConfiguration(name, filter, layout, true, Property.EMPTY_ARRAY, SASURL, loggerPrefix,
					prefix2);
		} catch (StorageException e) {
			throw new RuntimeException(SASURL + " is invalid.", e);
		}
	}

	private static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

}
